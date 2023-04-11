commitMessage=${commit.message}

mkdir -p ../openapi/${swagger.folder}
cp -R ../export/swagger.yaml ./${swagger.folder}/${swagger.name}
git add --all
git commit -m "$commitMessage"

# delete local tag if exists (it may remains after a previous failed build)
if [ $(git tag -l "$commitMessage") ]
then
  git tag -d $commitMessage # tag exists -> delete local tag
fi

git tag -l
git tag -a $commitMessage -m "$commitMessage" # create tag
git push --atomic origin HEAD:master tag "$commitMessage" # push with tag
