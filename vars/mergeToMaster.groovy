def call() {
    
    sh """
        echo "Merging to Master"
        git remote set-head origin master
        git checkout -B master origin/master
        git pull origin master
        git pull origin staging
        git push --set-upstream origin master
       """
}