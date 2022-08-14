#!/bin/bash

function waitFor(){
    for i in $(seq 1 $2); do
        ping -c 1 -w 1 -q $1 > /dev/null
        if [[ $? != 0 ]];then     
            echo "$(date) : wati for $1 ..."
            if [ $i -lt $2 ]; then
                sleep 2
            else
                echo "wait $1 timeout!!!"
                break    
            fi
        else                
            echo "$1 start ok"
            break
        fi
    done
}

function watiForLoop(){
    echo "watiForLoop $1"
    ary=(`echo $1 | tr '/' ' '`)
    for key in "${!ary[@]}"; do
        item=${ary[$key]}
        waitFor $item 20
    done
}

watiForLoop $1