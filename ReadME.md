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
