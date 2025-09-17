for i in {1..5}; do
  echo "Sending request number $i"
  curl -X POST "http://localhost:8088/api/v1/job"   -H "Content-Type: multipart/form-data"   -F "imagem=@/home/luiz/springboot/proc_images/pixelpro/src/main/resources/images/maca.jpg"   -F "dados={\"operationType\":\"SEPIA\"};type=application/json"
done
