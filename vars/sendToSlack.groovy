def call(message, messageType, build) {
    
    def color = '#D3D3D3'

    if (messageType == 'success') {
        color = '#008000'
    } else if (messageType == 'inProgress') {
        color = '#FFFF00'
    }
    else if (messageType == 'failure') {
        color = '#FF0000'
    }
    
    slackSend (color: color, message: "${message}: `${env.JOB_NAME}` [${build}] \n(<${env.BUILD_URL}|Open>)")
}