<template>
    <div class="container">
      <div class="columns is-centered">
        <div class="column is-half">
          <h1 class="title">Publish Event</h1>
          <form @submit.prevent="publish">
            <div class="field">
              <label class="label">Title</label>
              <div class="control">
                <input class="input" type="text" v-model="title" placeholder="Enter the event title">
              </div>
            </div>
            <div class="field">
              <label class="label">Time</label>
              <div class="control">
                <input class="input" type="datetime-local" v-model="time">
              </div>
            </div>
            <div class="field">
              <label class="label">Street</label>
              <div class="control">
                <input class="input" type="text" v-model="location.street" placeholder="Enter the street name">
              </div>
            </div>
            <div class="field">
              <label class="label">Number</label>
              <div class="control">
                <input class="input" type="number" v-model="location.number" placeholder="Enter the number">
              </div>
            </div>
            <div class="field">
              <label class="label">City</label>
              <div class="control">
                <input class="input" type="text" v-model="location.city" placeholder="Enter the city">
              </div>
            </div>
            <div class="field">
              <label class="label">Country</label>
              <div class="control">
                <input class="input" type="text" v-model="location.country" placeholder="Enter the country">
              </div>
            </div>
            <div class="field">
              <label class="label">Postal Code</label>
              <div class="control">
                <input class="input" type="number" v-model="location.postalCode" placeholder="Enter the postal code">
              </div>
            </div>
            <div class="field">
              <label class="label">Participation Limit</label>
              <div class="control">
                <input class="input" type="number" v-model="participationLimit" placeholder="Enter the participation limit">
              </div>
            </div>
            <div class="field">
              <label class="label">Tags</label>
              <div class="control">
                <Multiselect
                  v-model="multiValue"
                  mode="tags"
                  :close-on-select="false"
                  :searchable="true"
                  :create-option="true"
                  :options="source"
                  option-label="label"
                  option-value="value"
                  placeholder="Pick some tags to describe the event"
                />
              </div>
          </div>
            <div class="field">
              <label class="label">Description</label>
              <div class="control">
                <textarea class="textarea" v-model="description" placeholder="Enter the description" rows="7"></textarea>
              </div>
            </div>
            <div class="field">
              <label class="label">Images</label>
              <input type="file" id="filepond" name="filepond" class="filepond" />
              <input type="hidden" v-model="uploadedImages" />
            </div>
            <div class="field">
              <div class="control">
                <button class="button is-primary">Publish</button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
</template>

<script>
import axios from "axios";
import { ref, reactive } from 'vue';
import Multiselect from '@vueform/multiselect';
import { useRouter } from 'vue-router';
import { onMounted } from 'vue';
import * as FilePond from 'filepond';
import BASE_URL from '../../../config/dev.env';
import {TAGS} from "../../../config/dev.env";
export default {
  components: {
    Multiselect,
    FilePond
  },
  setup() {
    const router = useRouter();
    const title = ref("");
    const time = ref("");
    const location = reactive({ street: "", number: "", city: "", country: "", postalCode: "" });
    const participationLimit = ref("");
    const description = ref("");
    const multiValue = ref([]);
    const uploadedImages = ref([]);
    const source = ref(TAGS);


    const publish = async () => {
        const imagesPayload = uploadedImages.value.length > 0 ? uploadedImages.value.map(image => ({
          bucket: image.bucket,
          key: image.key
        })) : undefined;
        const data = {
            title: title.value,
            time: new Date(time.value).toISOString(),
            location: {
            street: location.street,
            number: location.number,
            city: location.city,
            postalCode: location.postalCode,
            country: location.country
            },
            participationLimit: parseInt(participationLimit.value),
            description: description.value,
            tags: multiValue.value,

      };
      if (imagesPayload !== undefined) {
            data.images = imagesPayload;
            }
  
  console.log("Data to be sent:", data);
  try {
    let url_create_event = "http://localhost:9191/event-service/api/events/create";
    let url_create_event_my = "http://localhost:8084/api/events/create"
    const response = await axios.post(url_create_event, data, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
      }
    });
    console.log("Successfully published:", response.data);
    router.push({ path: "/" });
  } catch (error) {
    console.error("Error: ", error);
  }
  };

  onMounted(() => {
        const pond = FilePond.create(document.querySelector('.filepond'), {
            instantUpload: false,
            allowMultiple: true,
            maxFiles: 9,
            server: {
                process: async (fieldName, file, metadata, load, error, progress, abort) => {
                    try {
                        const { url, key } = await getUploadUrl(); // Get the URL for upload

                        // Upload the file using axios
                        const response = await axios.put(url, file, {
                            onUploadProgress: (e) => {
                                progress(e.lengthComputable, e.loaded, e.total);
                            },
                            headers: {
                                'Content-Type': 'binary'
                            },
                        });

                        uploadedImages.value.push({ "bucket": "img", "key": key }); // Store the uploaded image info
                        console.log(uploadedImages.value[0]);
                        load(key); // Indicate successful upload
                    } catch (uploadError) {
                        console.error("Upload error:", uploadError);
                        error('Upload error');
                    }
                }
            }
        });
    });


    // Fetching upload URL
    const getUploadUrl = async () => {
        try {
            let url_get_upload = "http://localhost:9191/event-service/api/events/get_upload_image_url";
            let url_get_upload_my = "http://localhost:8084/api/events/get_upload_image_url";

            const response = await axios.get(url_get_upload,
            {headers: {
              'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            },
            });
            return {
                url: response.data.image.urls[0].url,
                key: response.data.image.key
            };
        } catch (error) {
            console.error("Error fetching upload URL:", error);
            throw error;
        }
    };


    return {
      title, time, location, participationLimit, description, publish, multiValue, source
    };
  }
}
</script>

<style scoped>
@import "@vueform/multiselect/themes/default.css";
@import 'filepond/dist/filepond.min.css';
.container {
  margin-top: 50px;
}
.textarea {
  min-height: 150px;
  width: 100%;
}
</style>
