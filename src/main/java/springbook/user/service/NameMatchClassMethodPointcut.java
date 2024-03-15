package springbook.user.service;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.util.PatternMatchUtils;

public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut {
    private String mappedName;
    private String classMappedName;

    public void setMappedName(String mappedName) {
        super.setMappedName(mappedName);
    }
    public void setClassMappedName(String classMappedName) {
        this.classMappedName = classMappedName;
    }

    @Override
    public ClassFilter getClassFilter() {
        return new ClassFilter() {
            @Override
            public boolean matches(Class<?> clazz) {
//                return clazz.getSimpleName().startsWith(classMappedName);
                return PatternMatchUtils.simpleMatch(classMappedName, clazz.getSimpleName());
            }
        };
    }
}
