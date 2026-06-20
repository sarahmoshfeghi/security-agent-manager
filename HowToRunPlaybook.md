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
