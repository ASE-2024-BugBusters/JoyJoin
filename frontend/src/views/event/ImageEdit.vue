<template>
  <div class="container">
    <div class="columns is-centered">
      <div class="column is-half">
        <h1 class="title">Upload Images</h1>
          <div class="field">
            <label class="label">Images</label>
            <input type="file" id="filepond" name="filepond" class="filepond" />
          </div>
          <div class="field">
            <div class="control">
              <button class="btn btn-outline-secondary" @click="returnToEventView"><strong>Return</strong></button>
              <button class="btn btn-outline-success" @click="upload"><strong>Upload</strong></button>
            </div>
          </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { computed, ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { onMounted } from 'vue';
import * as FilePond from 'filepond';
import {INTEREST_TAGS, BASE_URL_EVENT_SERVICE} from "../../../config/dev.env";
export default {
  components: {
    FilePond
  },
  setup() {
    const router = useRouter();
    const uploadedImages = ref([]);
    const eventId = router.currentRoute.value.params.eventId;
    const returnToEventView = () => {
        router.push({name: 'EventView', params: {eventId}});
    };
    const upload = async () => {
      const imagesPayload = uploadedImages.value.length > 0 ? uploadedImages.value.map(image => ({
        bucket: image.bucket,
        key: image.key
      })) : undefined;
      const data = {};
      if (imagesPayload !== undefined) {
        data.images = imagesPayload;
      }
      console.log("Data to be sent:", data);
      try {
        const updateImagesUrl = BASE_URL_EVENT_SERVICE + `/events/${eventId}`;
        console.log(data);
        const response = await axios.patch(updateImagesUrl, data, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
          }
        });
        console.log("Successfully uploaded:", response.data);
        // router.push({ path: "/" });
        router.push({ name: 'EventView', params: { eventId } });
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
        const getUploadUrl = BASE_URL_EVENT_SERVICE +"/events/get_upload_image_url";
        const response = await axios.get(getUploadUrl, {
          headers: {
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
      upload, returnToEventView
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

.field {
  flex-grow: 1;
  min-width: 300px;
}
.control {
  display: grid;
  grid-auto-flow: column;
  gap: 10px;  /* This sets the gap between any grid items */
}
.btn {
  font-weight: bold;
}
</style>
