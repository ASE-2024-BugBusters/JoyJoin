<template>
  <div class="container">
    <div class="columns is-centered">
      <div class="column is-half">
        <h1 class="title">Publish Event</h1>
        <form @submit.prevent="publish">
          <div class="field">
            <label class="label">Title</label>
            <div class="control">
              <input class="input" type="text" v-model="title" placeholder="Enter the event title" required>
            </div>
          </div>
          <div class="field">
            <label class="label">Time</label>
            <div class="control">
              <input class="input" type="datetime-local" v-model="time" :min="minDateTime" required>
            </div>
          </div>
          <div class="flex-row">
            <div class="field">
              <label class="label">Street</label>
              <div class="control">
                <input class="input" type="text" v-model="location.street" placeholder="Enter the street name" required>
              </div>
            </div>
            <div class="field">
              <label class="label">Number</label>
              <div class="control">
                <input class="input" type="number" v-model="location.number" placeholder="Enter the number" required>
              </div>
            </div>
            <div class="field">
              <label class="label">City</label>
              <div class="control">
                <input class="input" type="text" v-model="location.city" placeholder="Enter the city" required>
              </div>
            </div>
            <div class="field">
              <label class="label">Country</label>
              <div class="control">
                <input class="input" type="text" v-model="location.country" placeholder="Enter the country" required>
              </div>
            </div>
          </div>

          <div class="field">
            <label class="label">Participation Limit</label>
            <div class="control">
              <input class="input" type="number" v-model="participationLimit" placeholder="Enter the participation limit" required>
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
                  :create-option="false"
                  :options="source"
                  option-label="label"
                  option-value="value"
                  placeholder="Pick some tags to describe the event"
                  required
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
import { computed, ref, reactive } from 'vue';
import Multiselect from '@vueform/multiselect';
import { useRouter } from 'vue-router';
import { onMounted } from 'vue';
import * as FilePond from 'filepond';
import {INTEREST_TAGS, BASE_URL_EVENT_SERVICE} from "../../../config/dev.env";
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
    const source = ref(INTEREST_TAGS);
    const minDateTime = computed(() => {
      let now = new Date();
      return now.toISOString().substring(0, 16); // YYYY-MM-DDTHH:MM format
    });
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
      if (!title.value.trim() || !time.value || !multiValue.value.length ||
          !location.street.trim() || !location.number || !location.city.trim() ||
          !location.country.trim() || !participationLimit.value) {
        alert('Please fill all the required fields.');
        return;
      }
      if (imagesPayload !== undefined) {
            data.images = imagesPayload;
            }
  
  console.log("Data to be sent:", data);
  try {
    let createEventUrl = BASE_URL_EVENT_SERVICE + "/events/create";
    let createEventUrlTest = "http://localhost:8084/api/events/create"
    console.log(createEventUrl);
    console.log(createEventUrlTest);
    const response = await axios.post(createEventUrl, data, {
      headers: {
        // 'Content-Type': 'application/json',
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
            const getUploadUrl = "http://localhost:9191/event-service/api/events/get_upload_image_url";
            const getUploadUrlTest = BASE_URL_EVENT_SERVICE + "/events/get_upload_image_url";

            const response = await axios.get(getUploadUrl,
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
      title, time, location, participationLimit, description, publish, multiValue, source,minDateTime
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
.subtitle {
  font-weight: bold;
}
.textarea {
  min-height: 150px;
  width: 100%;
}
.flex-row {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.field {
  flex-grow: 1;
  min-width: 300px;
}
</style>
