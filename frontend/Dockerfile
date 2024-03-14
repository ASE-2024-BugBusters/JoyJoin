# Use a lightweight Node.js image as a base
FROM node:14-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and yarn.lock into the container
COPY package.json yarn.lock ./

# Install dependencies using Yarn
RUN yarn install

# Copy the rest of the application code into the container
COPY . .

# Build the Vue.js application for production
RUN yarn build

# Expose port 80 to the outside world
EXPOSE 80

# Command to run the application
CMD ["yarn", "serve"]
