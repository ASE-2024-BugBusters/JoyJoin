# Use a lightweight Node.js image as a base
FROM node:14-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the package.json and yarn.lock files into the container
COPY package.json yarn.lock ./

# Install dependencies using Yarn
RUN yarn install

# Copy the rest of the application code into the container
COPY . .

# Build the Vue.js application for production
RUN yarn build

# Use a lightweight HTTP server to serve the static files
FROM nginx:alpine

# Copy the built static files from the 'build' stage into the nginx server
COPY --from=build /app/dist /usr/share/nginx/html

# Expose port 80
EXPOSE 3000

# Start the nginx server when the container starts
CMD ["nginx", "-g", "daemon off;"]
