#!/bin/bash
URL=$1
PRODUCT_ID=$2
QUANTITY=$3
REQUESTS=$4

if [ -z "$4" ]; then
  echo "Usage: ./concurrent-test.sh <url> <product_id> <quantity> <number_of_requests>"
  echo "Example: ./concurrent-test.sh http://localhost:8080/api/orders/pessimistic 1 1 5"
  exit 1
fi

echo "Starting $REQUESTS concurrent requests to $URL"

for i in $(seq 1 $REQUESTS)
do
  # Fire requests in the background
  curl -s -X POST -H "Content-Type: application/json" \
       -d "{\"productId\": $PRODUCT_ID, \"quantity\": $QUANTITY, \"userId\": $i}" \
       $URL &
done

wait # Wait for all background jobs to finish
echo ""
echo "All requests completed."
