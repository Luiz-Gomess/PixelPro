for i in {1..5}; do
  echo "Sending request number $i"
  curl -X POST http://localhost:8080/api/v1/job  -H "Content-Type: application/json" -d '{"operationType": "GRAYSCALE", "originalImage": "image'$i'"}'
done
