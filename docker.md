docker build -t games/docker-sudoku:1.0-SNAPSHOT . 
docker run -d -p 10122:10122 games/docker-sudoku:1.0-SNAPSHOT
