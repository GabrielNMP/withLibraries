package pro.sky.java.course2.withlibraries.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pro.sky.java.course2.withlibraries.book.Employee;
import pro.sky.java.course2.withlibraries.exception.EmployeeAlredyInnExceptions;
import pro.sky.java.course2.withlibraries.exception.EmployeeNotFoudExceptions;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final Map<String,Employee> employeeMap;

    public EmployeeServiceImpl() {
        this.employeeMap = new HashMap<>();
    }

    private String getKey(Employee employee){
        return employee.getFirstName() + employee.getLastName();
    }

    //поиск сотрудника
    @Override
    public Employee passEmployee(String firstName, String lastName, int dept, int salary) {
        Employee employee = new Employee(firstName,lastName, dept, salary);
        if (employeeMap.containsKey(getKey(employee))){
            return employee;
        }
        throw new EmployeeNotFoudExceptions();
    }

    // добавление сотрудника
    @Override
    public Employee addEmployee(String firstName, String lastName, int dept, int salary) {
        Employee employee = new Employee(firstName,lastName, dept, salary);
        if (employeeMap.containsKey(getKey(employee))){
            throw new EmployeeAlredyInnExceptions();
        }
        else if (!StringUtils.isAlpha(firstName) || !StringUtils.isAlpha(lastName)) {
            throw new RuntimeException("404 Bad Request");
        }
        else if (!Character.isUpperCase(firstName.charAt(0)) || !Character.isUpperCase(lastName.charAt(0))) {
            StringUtils.capitalize(firstName);
            StringUtils.capitalize(lastName);
            employeeMap.put(getKey(employee),employee);
        }
        return employee;
    }

    // удаление сотрудника
    @Override
    public Employee deleteEmployee(String firstName, String lastName, int dept, int salary) {
        Employee employee = new Employee(firstName, lastName, dept, salary);
        if (employeeMap.containsKey(getKey(employee))){
            employeeMap.remove(getKey(employee),employee);
            return employee;
        }
        throw new EmployeeNotFoudExceptions();
    }

    //весь список отсортированный по отделам
    @Override
    public Map<Integer, Set<Employee>> finedAll() {
        return employeeMap.keySet().stream()
                .map(d ->employeeMap.get(d))
                .collect(Collectors.groupingBy(Employee::getDept,Collectors.toSet()));
    }

    // сотрудник в отделе с максимальной зп
    @Override
    public Employee maxSalary(int dept) {
        return employeeMap.entrySet().stream()
                .map(e -> e.getValue())
                .filter(p -> p.getDept() == dept)
                .max(Comparator.comparingInt(c -> c.getSalary())).get();
    }

    //сотрудник в отделе с минимальной зп
    @Override
    public Employee minSalary(int dept) {
        return employeeMap.entrySet().stream()
                .map(e -> e.getValue())
                .filter(p -> p.getDept() == dept)
                .min(Comparator.comparingInt(c -> c.getSalary())).get();
    }

    // список сотрудников определённого отдела
    @Override
    public Map<Integer, Set<Employee>> deptList (int dept) {
        return employeeMap.keySet().stream()
                .filter(n -> employeeMap.get(n).getDept() ==dept)
                .map(n -> employeeMap.get(n))
                .collect(Collectors.groupingBy(Employee::getDept,Collectors.toSet()));
    }


}
