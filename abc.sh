
C=0
while [ $C -lt 50 ]; do
	curl -X POST -d msg="teste"$C http://54.68.22.186:9000/post
	D=0
	while [ $D -lt 10 ]; do
	        curl -X POST -d comment="teste"$D http://54.68.22.186:9000/post/$C/comment
	        let D=D+1
	done

	let C=C+1
done
