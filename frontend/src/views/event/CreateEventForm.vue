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

export default {
  components: {
    Multiselect
  },
  setup() {
    const router = useRouter();
    const title = ref("");
    const time = ref("");
    const location = reactive({ street: "", number: "", city: "", country: "", postalCode: "" });
    const participationLimit = ref("");
    const description = ref("");
    const multiValue = ref([]); // MultiSelect v-model
    const source = ref([
        { value: 'VEGAN_CUISINE', label: 'Vegan Cuisine' },
        { value: 'NIGHTLIFE', label: 'Nightlife' },
        { value: 'PUB_CRAWLS', label: 'Pub Crawls' },
        { value: 'CONCERTS', label: 'Concerts' },
        { value: 'BRUNCH', label: 'Brunch' },
        { value: 'MOCKTAILS', label: 'Mocktails' },
        { value: 'COCKTAILS', label: 'Cocktails' },
        { value: 'VEGETARIANISM', label: 'Vegetarianism' },
        { value: 'CRAFT_BEER', label: 'Craft Beer' },
        { value: 'WINE_TASTING', label: 'Wine Tasting' },
        { value: 'GASTRONOMY', label: 'Gastronomy' },
        { value: 'EXERCISE', label: 'Exercise' },
        { value: 'SOCCER', label: 'Soccer' },
        { value: 'BASKETBALL', label: 'Basketball' },
        { value: 'HEALTHY_EATING', label: 'Healthy Eating' },
        { value: 'HIKING', label: 'Hiking' },
        { value: 'YOGA', label: 'Yoga' },
        { value: 'MEDITATION', label: 'Meditation' },
        { value: 'PHOTOGRAPHY', label: 'Photography' },
        { value: 'TRAVEL', label: 'Travel' },
        { value: 'READING', label: 'Reading' },
        { value: 'COOKING', label: 'Cooking' },
        { value: 'GAMING', label: 'Gaming' },
        { value: 'PAINTING', label: 'Painting' },
        { value: 'MUSIC', label: 'Music' },
        { value: 'DANCING', label: 'Dancing' },
        { value: 'FASHION', label: 'Fashion' },
        { value: 'WRITING', label: 'Writing' },
        { value: 'GARDENING', label: 'Gardening' },
        { value: 'FILM', label: 'Film' },
        { value: 'THEATER', label: 'Theater' },
        { value: 'ARCHITECTURE', label: 'Architecture' },
        { value: 'HISTORY', label: 'History' },
        { value: 'SCIENCE', label: 'Science' },
        { value: 'TECHNOLOGY', label: 'Technology' },
        { value: 'WILDLIFE', label: 'Wildlife' },
        { value: 'CONSERVATION', label: 'Conservation' },
        { value: 'SUSTAINABILITY', label: 'Sustainability' },
        { value: 'ENTREPRENEURSHIP', label: 'Entrepreneurship' },
        { value: 'VOLUNTEERING', label: 'Volunteering' },
        { value: 'SURFING', label: 'Surfing' }
    ]);


    const publish = async () => {
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
            tags: multiValue.value
      };
  
  console.log("Data to be sent:", data);
  try {
    const response = await axios.post("http://localhost:9191/event-service/events/create", data, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    console.log("Successfully published:", response.data);
    router.push({ path: "/" });
  } catch (error) {
    console.error("Error: ", error);
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

.container {
  margin-top: 50px;
}
.textarea {
  min-height: 150px;
  width: 100%;
}
</style>
