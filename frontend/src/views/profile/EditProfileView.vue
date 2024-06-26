<template>
  <div class="container">
    <div class="columns is-centered">
      <div class="column is-half">
        <h1 class="title">Edit Profile</h1>
        <form @submit.prevent="publish">
          <div class="field">
            <label class="label">Account Name</label>
            <div class="control">
              <input class="input" type="text" v-model="accountName" placeholder="Enter your account name">
            </div>
          </div>
          <div class="field">
            <label class="label">First Name</label>
            <div class="control">
              <input class="input" type="text" v-model="firstName" placeholder="Enter your first name">
            </div>
          </div>
          <div class="field">
            <label class="label">Last Name</label>
            <div class="control">
              <input class="input" type="text" v-model="lastName" placeholder="Enter your last name">
            </div>
          </div>
          <div class="field">
            <label class="label">Biography</label>
            <div class="control">
              <textarea class="textarea" v-model="biography" placeholder="Enter your Bio" rows="7"
                        maxlength="255"></textarea>
            </div>
          </div>
          <div class="field">
            <label class="label">Tags</label>
            <div class="control">
              <Multiselect
                  v-model="interestTags"
                  mode="tags"
                  :close-on-select="false"
                  :searchable="true"
                  :create-option="true"
                  :options="source"
                  option-label="label"
                  option-value="value"
                  placeholder="Pick some tags to describe your interests"
              />
            </div>
          </div>
          <div class="field">
            <label class="label">Avatar</label>
            <input type="file" id="filepond" name="filepond" class="filepond"/>
            <input type="hidden" v-model="avatar"/>
          </div>
          <div class="field">
            <div class="control">
              <button class="button is-primary">Update</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

import {ref, reactive} from 'vue';
import Multiselect from '@vueform/multiselect';
import {useRouter} from 'vue-router';
import {onMounted} from 'vue';
import * as FilePond from 'filepond';
import {BASE_URL_USER_SERVICE, INTEREST_TAGS} from '../../../config/dev.env';

export default {
  components: {
    Multiselect,
    FilePond
  },
  methods: {
    async fetchUserProfile() {
      try {
        const response = await axios.get(BASE_URL_USER_SERVICE + '/user/users/' + sessionStorage.userId, {
          headers: {
            "Authorization": `Bearer ${sessionStorage.jwtToken}`
          }
        });
        const data = response.data[0];
        this.accountName = data.accountName
        this.firstName = data.firstName
        this.lastName = data.lastName
        this.biography = data.biography
        this.interestTags = []
        if (data.interestTags) {
          for (const tag of data.interestTags) {
            this.interestTags.push(tag)
          }
        }
        this.avatar = {
          bucket: "",
          key: ""
        }
        if (data.avatar) {
          this.avatar = {
            bucket: data.avatar.bucket,
            key: data.avatar.key
          }
        }
      } catch (e) {
        console.error(e)
        alert("Failed to fetch user profile")
      }
    }
  },
  created() {
    this.fetchUserProfile()
  },

  setup() {
    const router = useRouter();
    const accountName = ref("");
    const firstName = ref("");
    const lastName = ref("");
    const biography = ref("");
    const interestTags = ref([]); // MultiSelect v-model
    const avatar = ref({
      bucket: "",
      key: ""
    });

    const source = ref(JSON.parse(JSON.stringify(INTEREST_TAGS)));

    const publish = async () => {
      const data = {
        accountName: accountName.value,
        firstName: firstName.value,
        lastName: lastName.value,
        biography: biography.value,
        interestTags: interestTags.value,
        avatar: avatar.value
      };

      console.log("Data to be sent:", data);
      try {
        let url_update_profile = BASE_URL_USER_SERVICE + '/user/users/' + sessionStorage.userId
        const response = await axios.patch(url_update_profile, data, {
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${sessionStorage.jwtToken}`
          }
        });
        console.log("Successfully published:", response.data);
        await router.push({path: "/profile/" + sessionStorage.userId});
      } catch (error) {
        console.error("Error: ", error);
      }
    };

    onMounted(() => {
      const pond = FilePond.create(document.querySelector(".filepond"), {
        instantUpload: false,
        allowMultiple: false,
        server: {
          process: async (fieldName, file, metadata, load, error, progress, abort) => {
            try {
              const {url, bucket, key} = await getUploadUrl(); // Get the URL for upload

              // Upload the file using axios
              const response = await axios.put(url, file, {
                onUploadProgress: (e) => {
                  progress(e.lengthComputable, e.loaded, e.total);
                },
                headers: {
                  "Content-Type": "binary"
                },
              });

              avatar.value = {"bucket": bucket, "key": key}
              load(key) // Indicate successful upload
            } catch (uploadError) {
              console.error("Upload error:", uploadError);
              error("Upload error");
            }
          }
        }
      });
    });

    // Fetching upload URL
    const getUploadUrl = async () => {
      try {
        let url_get_upload = BASE_URL_USER_SERVICE + '/user/users/' + sessionStorage.userId + "/upload_avatar"
        const response = await axios.get(url_get_upload, {
          headers: {
            "Authorization": `Bearer ${sessionStorage.jwtToken}`
          }
        });
        console.log(response.data)
        return {
          url: response.data.image.urls[0].url,
          bucket: response.data.image.bucket,
          key: response.data.image.key
        };
      } catch (error) {
        console.error("Error fetching upload URL:", error);
        throw error;
      }
    };

    return {
      accountName, firstName, lastName, biography, interestTags, avatar, publish, source
    };
  }
}
</script>

<style scoped>
@import "@vueform/multiselect/themes/default.css";
@import "filepond/dist/filepond.min.css";

.container {
  margin-top: 50px;
}

.textarea {
  min-height: 150px;
  width: 100%;
}
</style>
