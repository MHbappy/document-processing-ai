# ---- Stage 1: Build ----
    FROM node:22-alpine AS builder

    WORKDIR /app
    
    # Copy only package files first (for caching)
    COPY package*.json ./
    
    # Install dependencies
    RUN npm ci
    
    # Copy all project files
    COPY . .
    
    # Build Next.js (TypeScript → JS)
    RUN npm run build
    
    # ---- Stage 2: Production ----
    FROM node:22-alpine AS runner
    
    WORKDIR /app
    
    ENV NODE_ENV=production
    
    # Copy necessary files from builder
    COPY --from=builder /app/public ./public
    COPY --from=builder /app/.next ./.next
    COPY --from=builder /app/package.json ./package.json
    COPY --from=builder /app/node_modules ./node_modules
    COPY --from=builder /app/next.config.ts ./next.config.ts
    
    # Expose port 3000
    EXPOSE 3000
    
    # Start Next.js in production mode
    CMD ["npm", "start"]
    