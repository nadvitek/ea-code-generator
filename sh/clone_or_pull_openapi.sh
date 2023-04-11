#!/bin/bash
REPOSITORY=$(git ls-remote openapi)
if [ -z "$REPOSITORY" ] # open-api repository does not exist
then
	    echo ">>> Clone open-api <<<"
      git clone git@gitlab.cc.corp:cprdigital/daf/shared/open-api.git openapi
else
	    echo ">>> Pull open-api <<<"
      cd ./openapi
	    git pull origin master
	    cd ..
fi

