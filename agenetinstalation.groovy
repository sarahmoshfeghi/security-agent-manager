pipeline {
    agent any

    parameters {
        choice(
            name: 'TARGET_TAG', 
            choices: ['all', 'splunk', 'tetragon', 'cadvisor', 'zabbix', 'node_exporter', 'security', 'monitoring'], 
            description: 'Select which specific agent lifecycle task to execute. Selecting "all" evaluates all agents.'
        )
    }

    environment {
        ANSIBLE_FORCE_COLOR = 'true'
    }

    stages {
        stage('Code Checkout') {
            steps {
                cleanWs()
                checkout scm
            }
        }

        stage('Ansible Lint & Syntax Check') {
            steps {
                echo "Checking playbooks for structural and syntax validity..."
                sh "ansible-playbook -i inventory.ini site.yml --syntax-check"
            }
        }

        stage('Execute Agent Lifecycle') {
            steps {
                script {
                    // Check if running all configurations or utilizing explicit targeting tags
                    if (params.TARGET_TAG == 'all') {
                        echo "Executing global agent state configurations..."
                        sh "ansible-playbook -i inventory.ini site.yml"
                    } else {
                        echo "Targeting specific agent tag scope: ${params.TARGET_TAG}"
                        sh "ansible-playbook -i inventory.ini site.yml --tags ${params.TARGET_TAG}"
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Infrastructure agent changes successfully applied."
        }
        failure {
            echo "Pipeline failed. Please review the Ansible tasks execution logs above."
        }
    }
}
