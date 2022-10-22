/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  swcMinify: true,
  // TO CONFIUGURE FOR PRODUCTION
  // NOW ONLY MOCK DOMAINS
  images: {
    domains: ['placeimg.com']
  }
}

module.exports = nextConfig
