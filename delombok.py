import os
import re

def process_java_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # If no lombok annotations, skip
    if 'lombok' not in content and '@Slf4j' not in content and '@Getter' not in content and '@RequiredArgsConstructor' not in content:
        return

    # Remove lombok imports
    content = re.sub(r'import lombok\..*;\n', '', content)
    
    # 1. Handle @Slf4j
    if '@Slf4j' in content:
        content = content.replace('@Slf4j', '')
        # add logger to class
        class_match = re.search(r'public class (\w+)', content)
        if class_match:
            class_name = class_match.group(1)
            logger_decl = f'\n    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger({class_name}.class);\n'
            # insert after class declaration
            content = re.sub(r'(public class ' + class_name + r'.*?{)', r'\1' + logger_decl, content, count=1, flags=re.DOTALL)

    # 2. Handle @RequiredArgsConstructor
    if '@RequiredArgsConstructor' in content:
        content = content.replace('@RequiredArgsConstructor', '')
        # find final fields
        final_fields = re.findall(r'private final (\w+(?:<[^>]+>)?)\s+(\w+);', content)
        if final_fields:
            class_match = re.search(r'public class (\w+)', content)
            if class_match:
                class_name = class_match.group(1)
                constructor_args = ', '.join([f'{t} {n}' for t, n in final_fields])
                constructor_assignments = '\n'.join([f'        this.{n} = {n};' for t, n in final_fields])
                constructor = f'\n    public {class_name}({constructor_args}) {{\n{constructor_assignments}\n    }}\n'
                # insert after last field or before first method
                # Simple approach: append to class end before the last '}'
                content = content.rsplit('}', 1)[0] + constructor + '}\n'

    # 3. Handle @Getter / @Setter / @NoArgsConstructor / @AllArgsConstructor / @Builder
    has_builder = '@Builder' in content
    if '@Getter' in content or '@Setter' in content or has_builder:
        content = content.replace('@Getter', '').replace('@Setter', '').replace('@NoArgsConstructor', '').replace('@AllArgsConstructor', '').replace('@Builder.Default', '').replace('@Builder', '')
        
        # Extract fields
        # Regex to match: private [Type] [name]; OR private [Type] [name] = [value];
        fields = re.findall(r'private\s+(?!static)(?!final)([\w<>,\s]+)\s+(\w+)(?:\s*=\s*([^;]+))?;', content)
        
        methods = []
        class_match = re.search(r'public class (\w+)', content)
        if not class_match:
            class_match = re.search(r'public static class (\w+)', content)
            
        if class_match and fields:
            class_name = class_match.group(1)
            
            # Constructors
            no_args = f'\n    public {class_name}() {{}}\n'
            all_args_params = ', '.join([f'{t.strip()} {n}' for t, n, v in fields])
            all_args_body = '\n'.join([f'        this.{n} = {n};' for t, n, v in fields])
            all_args = f'\n    public {class_name}({all_args_params}) {{\n{all_args_body}\n    }}\n'
            methods.extend([no_args, all_args])
            
            # Getters and Setters
            for t, n, v in fields:
                t = t.strip()
                cap_n = n[0].upper() + n[1:]
                getter = f'\n    public {t} get{cap_n}() {{ return this.{n}; }}\n'
                setter = f'\n    public void set{cap_n}({t} {n}) {{ this.{n} = {n}; }}\n'
                methods.extend([getter, setter])
                
            # Builder
            if has_builder:
                builder_class = f'\n    public static class {class_name}Builder {{\n'
                for t, n, v in fields:
                    t = t.strip()
                    if v:
                        builder_class += f'        private {t} {n} = {v};\n'
                    else:
                        builder_class += f'        private {t} {n};\n'
                
                for t, n, v in fields:
                    t = t.strip()
                    builder_class += f'\n        public {class_name}Builder {n}({t} {n}) {{\n            this.{n} = {n};\n            return this;\n        }}\n'
                
                build_args = ', '.join([f'this.{n}' for t, n, v in fields])
                builder_class += f'\n        public {class_name} build() {{\n            return new {class_name}({build_args});\n        }}\n    }}\n'
                
                builder_method = f'\n    public static {class_name}Builder builder() {{ return new {class_name}Builder(); }}\n'
                
                methods.extend([builder_class, builder_method])
                
            content = content.rsplit('}', 1)[0] + ''.join(methods) + '}\n'

    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f"Processed {filepath}")

src_dir = r"d:\votesecure\src\main\java\com\votesecure"
for root, dirs, files in os.walk(src_dir):
    for f in files:
        if f.endswith('.java'):
            process_java_file(os.path.join(root, f))
