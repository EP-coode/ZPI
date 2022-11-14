/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  // TO CONFIUGURE FOR PRODUCTION
  // NOW ONLY MOCK DOMAINS
  images: {
    domains: ["placeimg.com"],
  },
  env: {
    AUTH_SERVICE_URL: process.env.AUTH_SERVICE_URL,
    DATA_PROVIDER_URL: process.env.DATA_PROVIDER_URL,
  },
};

module.exports = nextConfig;
