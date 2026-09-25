CREATE INDEX idx_task_employee_in_charge
    ON task(employee_in_charge_id);

CREATE INDEX idx_task_current_status
    ON task(current_status);

CREATE INDEX idx_task_deadline
    ON task(deadline);