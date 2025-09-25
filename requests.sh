# Requesting examples

for i in {1..5}; do
  echo "Sending request number $i"
  curl -X POST "http://localhost:8089/api/v1/job" \
    -H "Content-Type: multipart/form-data" \
    -F "imagem=@./src/main/resources/images/maca.jpg" \
    -F "dados={\"operationType\":\"SEPIA\"};type=application/json"
done
