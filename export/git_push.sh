commitMessage=library-system_1.0.0.swagger.yaml

mkdir -p ../openapi/library-system/v1_0
cp -R ../export/swagger.yaml ./library-system/v1_0/library-system_1.0.0.swagger.yaml
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
