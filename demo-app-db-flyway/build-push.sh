#build Image
docker build . -t sumitgupta28/demo-app-fly-way:latest
#docker Login
docker login
#docker Push
docker push sumitgupta28/demo-app-fly-way:latest