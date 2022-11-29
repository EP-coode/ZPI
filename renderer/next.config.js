/** @type {import('next').NextConfig} */
const removeImports = require('next-remove-imports')();

const nextConfig = removeImports({
  reactStrictMode: true,
  swcMinify: true,
  // TO CONFIUGURE FOR PRODUCTION
  // NOW ONLY MOCK DOMAINS
  images: {
    domains: ["studentcommunityimages.blob.core.windows.net"],
  },
  env: {
    AUTH_SERVICE_URL: process.env.AUTH_SERVICE_URL,
    DATA_PROVIDER_URL: process.env.DATA_PROVIDER_URL,
  }
});

module.exports = nextConfig;
