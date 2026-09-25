CREATE TABLE employee (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE task (
                      id BIGSERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      description VARCHAR(255) NOT NULL,
                      deadline DATE NOT NULL,
                      priority VARCHAR(50) NOT NULL,
                      current_status VARCHAR(50) NOT NULL,
                      review VARCHAR(50) NOT NULL,
                      employee_in_charge_id BIGINT,

                      CONSTRAINT fk_task_employee
                          FOREIGN KEY (employee_in_charge_id)
                              REFERENCES employee(id)
);