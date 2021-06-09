package ${pkg};

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.*;

<#list imports as impt>
    import ${impt};
</#list>

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ${className}{

<#list fields as field>
    private ${field.fieldType} ${field.fieldName};
</#list>

}