package com.mycompany.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.List;

public class SpringBootGwt implements EntryPoint {

    private FlexTable empFlexTable = new FlexTable();
    private ArrayList<Employee> employeeList = new ArrayList<>();
    private EmployeeService employeeService = GWT.create(EmployeeService.class);
    private VerticalPanel mainPanel = new VerticalPanel();
    private HorizontalPanel addPanel = new HorizontalPanel();
    private TextBox nameText = new TextBox();
    private TextBox roleText = new TextBox();
    private Button addEmployeeBtn = new Button("add employee");

    public void onModuleLoad() {
        useCorrectRequestBaseUrl();

        empFlexTable.setText(0, 0, "Id");
        empFlexTable.setText(0, 1, "Name");
        empFlexTable.setText(0, 2, "Role");
        empFlexTable.setText(0, 3, "Remove");
        empFlexTable.setCellPadding(6);

        addEmployeeBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addEmployee(nameText.getText(),roleText.getText());
            }
        });

        addPanel.add(nameText);
        addPanel.add(roleText);
        addPanel.add(addEmployeeBtn);

        addPanel.addStyleName("addPanel");

        mainPanel.add(empFlexTable);
        mainPanel.add(addPanel);

        RootPanel.get().add(mainPanel);

        refreshEmployees();
    }

    private void addEmployee(final String name, final String role) {
        employeeService.addEmployee(new Employee(name, role), new MethodCallback<Void>() {
            @Override
            public void onFailure(final Method method, final Throwable exception) {

            }

            @Override
            public void onSuccess(final Method method, final Void response) {
                nameText.setText("");
                roleText.setText("");

                refreshEmployees();
            }
        });
    }

    public void removeEmployee(final Employee employee) {
        employeeService.deleteEmployee(employee, new MethodCallback<Void>() {
            @Override
            public void onFailure(final Method method, final Throwable exception) {

            }

            @Override
            public void onSuccess(final Method method, final Void response) {
                int removedIndex = employeeList.indexOf(employee);
                employeeList.remove(removedIndex);
                empFlexTable.removeRow(removedIndex + 1);

                refreshEmployees();
            }
        });
    }

    private void refreshEmployees() {
        employeeService.getEmployees(new MethodCallback<List<Employee>>() {
            @Override
            public void onFailure(final Method method, final Throwable exception) {
            }

            @Override
            public void onSuccess(final Method method, final List<Employee> response) {
                for (Employee employee : response) {
                    if (employeeList.contains(employee)) {
                        continue;
                    }
                    employeeList.add(employee);

                    Button removeEmployeeBtn = new Button("X");
                    removeEmployeeBtn.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            removeEmployee(employee);
                        }
                    });

                    int row = empFlexTable.getRowCount();
                    empFlexTable.setText(row, 0, employee.getId().toString());
                    empFlexTable.setText(row, 1, employee.getName());
                    empFlexTable.setText(row, 2, employee.getRole());
                    empFlexTable.setWidget(row, 3, removeEmployeeBtn);
                }
            }
        });
    }

    private void useCorrectRequestBaseUrl() {
        if (isDevelopmentMode()) {
            Defaults.setServiceRoot("http://localhost:8080");
        } else {
            Defaults.setServiceRoot(GWT.getHostPageBaseURL());
        }
    }

    private static native boolean isDevelopmentMode()/*-{
        return typeof $wnd.__gwt_sdm !== 'undefined';
    }-*/;
}
