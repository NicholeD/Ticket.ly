# Step One- Fill out the UNIT_FOUR_REPO_NAME and GITHUB_USERNAME

# Step Two - configure your shell to always have these variables.
# For OSX / Linux
# Copy and paste ALL of the properties below into your .bash_profile in your home directly

# For Windows
# Copy and paste ALL of the properties below into your .bashrc file in your home directory


# Fill out the following values
# The path of your repo on github.  Don't but the whole URL, just the part after github.com/
export UNIT_FOUR_REPO_NAME=ata-unit-four-project

# Do not modify the rest of these unless you have been instructed to do so.
export UNIT_FOUR_PROJECT_NAME=unitproject4
export UNIT_FOUR_PIPELINE_STACK=$UNIT_FOUR_PROJECT_NAME-$GITHUB_USERNAME
export UNIT_FOUR_ARTIFACT_BUCKET=$UNIT_FOUR_PROJECT_NAME-$GITHUB_USERNAME-artifacts
export UNIT_FOUR_DEPLOY_STACK=$UNIT_FOUR_PROJECT_NAME-$GITHUB_USERNAME-application
export UNIT_FOUR_APPLICATION_NAME=$UNIT_FOUR_PROJECT_NAME-$GITHUB_USERNAME-application
export UNIT_FOUR_ENVIRONMENT_NAME=$UNIT_FOUR_PROJECT_NAME-$GITHUB_USERNAME-environment-dev
