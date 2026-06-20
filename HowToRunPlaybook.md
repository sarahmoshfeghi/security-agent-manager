
# Linux Fleet Agent Manager

An automated configuration management framework using **Ansible** and **Jenkins (Groovy)** to manage the entire lifecycle—**Install (Add), Upgrade (Update), and Purge (Delete)**—of infrastructure, security, and monitoring agents across Linux fleets.

## 🚀 Supported Fleet Agents
* **Splunk Universal Forwarder (UF)** (`tags: [splunk]`)
* **Tetragon Security Agent** (`tags: [tetragon]`)
* **cAdvisor Container Monitor** (`tags: [cadvisor]`)
* **Zabbix Agent** (`tags: [zabbix]`)
* **Node Exporter** (`tags: [node_exporter]`)

---

## ⚙️ The State Matrix (How to Make Changes)

Instead of using different playbooks for adding or deleting agents, configurations are controlled entirely inside the variable matrix file: **`roles/agent_manager/defaults/main.yml`**.

To modify your infrastructure layout, open that file and configure your target states:
* Use **`"present"`** to Install or Upgrade an agent.
* Use **`"absent"`** to completely Uninstall, Stop, and Delete an agent.

---

## 🏃 Runbook: Manual Execution Commands

### 1. Run Everything (Standard Execution)
Applies the current global state configuration defined in `defaults/main.yml` across all target instances.
```bash
ansible-playbook -i inventory.ini site.yml

```

### 2. Scenario: Update/Install a Single Agent (e.g., Splunk)

1. Ensure `splunk_uf_state: "present"` and the version is set correctly in `defaults/main.yml`.
2. Target only the Splunk task file using tags:

```bash
ansible-playbook -i inventory.ini site.yml --tags splunk

```

### 3. Scenario: Completely Delete/Remove an Agent (e.g., Zabbix)

1. Open `roles/agent_manager/defaults/main.yml`.
2. Change the state variable to absent: `zabbix_state: "absent"`.
3. Execute the playbook targeting that agent tag:

```bash
ansible-playbook -i inventory.ini site.yml --tags zabbix

```

*Ansible will safely stop the Zabbix system service, purge the binaries, and wipe out configuration directories.*

### 4. Grouped Execution (e.g., Run Only Security Checks)

Runs tasks only matching the `security` tag metadata (Splunk and Tetragon):

```bash
ansible-playbook -i inventory.ini site.yml --tags security

```

---

## 🤖 CI/CD Orchestration via Jenkins

This repository includes a native Groovy `Jenkinsfile` for pipeline automation.

### Pipeline Features

* Automatically triggers dynamic parameters allowing engineers to run specific tags (`all`, `splunk`, `tetragon`, etc.) from the Jenkins GUI.
* Performs automatic code syntax and lint validation checking prior to running plays against live infrastructure targets.

```

---

### 📄 File 11: `Jenkinsfile`
* **Path:** `Jenkinsfile`
* **Description:** Declarative Jenkins Pipeline script written in **Groovy**. It auto-injects an execution parameter menu into the Jenkins interface so you can choose to deploy everything or target a specific agent during manual builds.

```groovy
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

```
