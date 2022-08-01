branch=main
if [ -z "$UNIT_FOUR_REPO_NAME" ] ; then
  echo "Your environment variables are not properly configured.  Make sure that you have filled out setupEnvironment.sh and that script is set to run as part of your PATH"
  exit 1
fi

if [ -z "$GITHUB_TOKEN" ] ; then
  echo "Your environment variable GITHUB_TOKEN is not properly configured.  Make sure that you have added it to your .bash_profile"
  exit 1
fi

if [ -z "$GITHUB_USERNAME" ] ; then
  echo "Your environment variable GITHUB_USERNAME is not properly configured.  Make sure that you have added it to your .bash_profile"
  exit 1
fi


echo "Outputting parameters for the pipeline..."
echo "Project name: $UNIT_FOUR_PROJECT_NAME"
echo "Github UserName: $GITHUB_USERNAME"
echo "Repo path: $UNIT_FOUR_REPO_NAME"
echo "Branch: $branch"

aws cloudformation create-stack --stack-name $UNIT_FOUR_PROJECT_NAME-$GITHUB_USERNAME --template-url https://ata-deployment-scripts.s3.us-east-1.amazonaws.com/CICDPipeline-Unit4.yml --parameters ParameterKey=ProjectName,ParameterValue=$UNIT_FOUR_PROJECT_NAME ParameterKey=GithubUserName,ParameterValue=$GITHUB_USERNAME ParameterKey=Repo,ParameterValue=$UNIT_FOUR_REPO_NAME ParameterKey=Branch,ParameterValue=$branch ParameterKey=GithubToken,ParameterValue=$GITHUB_TOKEN --capabilities CAPABILITY_IAM
