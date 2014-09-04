
C=0
while [ $C -lt 50 ]; do
	curl -X POST -d msg="teste"$C http://localhost:5050/post
	let C=C+1
done
