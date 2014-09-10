#passe como parametro o host + porta que o servidor está rodando
#exemplo localhost:8080

C=0
while [ $C -lt 50 ]; do
        curl -X POST -d msg="teste"$C http://$1/post
        D=0
        while [ $D -lt 10 ]; do
                curl -X POST -d comment="teste"$D http://$1/post/$C/comment
                let D=D+1
        done

        let C=C+1
done

