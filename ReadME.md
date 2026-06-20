# Linux Fleet Agent Manager

An automated, enterprise-grade configuration management tool using **Ansible** to manage the entire lifecycle—**Add (Install), Update, Remove, and Purge (Delete)**—of operational, security, and monitoring agents across Linux servers.

## 🚀 Supported Agents
* **Splunk Universal Forwarder (UF)** — Log collection and forwarding.
* **Tetragon** — eBPF-based security observability and runtime enforcement.
* **cAdvisor** — Container resource usage and performance analysis.
* **Zabbix Agent** — Infrastructure and application metrics collection.
* **Node Exporter** — Hardware and OS metrics for Prometheus.

---

## 📂 Repository Structure

```text
linux-agent-manager/
├── README.md
├── site.yml                     # Main playbook execution entrypoint
├── inventory.ini                # Target servers inventory list
└── roles/
    └── agent_manager/
        ├── defaults/
        │   └── main.yml         # Global versioning & state configurations
        └── tasks/
            ├── main.yml         # Main task orchestrator
            ├── splunk_uf.yml    # Lifecycle tasks for Splunk UF
            ├── tetragon.yml     # Lifecycle tasks for Tetragon
            ├── cadvisor.yml     # Lifecycle tasks for cAdvisor
            ├── zabbix.yml       # Lifecycle tasks for Zabbix Agent
            └── node_exporter.yml# Lifecycle tasks for Node Exporter
