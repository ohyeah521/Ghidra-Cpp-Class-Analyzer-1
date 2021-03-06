package ghidra.app.cmd.data.rtti.gcc;

import java.util.LinkedHashSet;
import java.util.Set;

import ghidra.app.cmd.data.rtti.ClassTypeInfo;
import ghidra.app.cmd.data.rtti.TypeInfo;
import ghidra.app.cmd.data.rtti.Vtable;
import ghidra.app.cmd.data.rtti.gcc.builder.AbstractTypeInfoProgramBuilder;
import ghidra.app.cmd.data.rtti.gcc.builder.Ppc64TypeInfoProgramBuilder;
import ghidra.app.cmd.data.rtti.gcc.builder.X86TypeInfoProgramBuilder;
import ghidra.program.model.address.Address;
import ghidra.program.model.data.InvalidDataTypeException;

import org.junit.Test;

public class VtableModelTest extends GenericGccRttiTest {

    private void validationTest(AbstractTypeInfoProgramBuilder builder) throws Exception {
        for (VtableModel vtable : builder.getVtableList()) {
            vtable.validate();
        }
    }

    private void locationTest(AbstractTypeInfoProgramBuilder builder) throws Exception {
        Set<Address> addresses = new LinkedHashSet<>();
        for (TypeInfo type : builder.getTypeInfoList()) {
            if (type instanceof ClassTypeInfo) {
                Vtable vtable = ((ClassTypeInfo) type).getVtable();
                try {
                    vtable.validate();
                    addresses.add(((VtableModel) vtable).getAddress());
                } catch (InvalidDataTypeException e) {}
            }
        }
        for (VtableModel vtable : builder.getVtableList()) {
            assert addresses.contains(vtable.getAddress())
                : vtable.getTypeInfo().getNamespace().getName(true);
        }
    }

    @Test
    public void defaultValidationTest() throws Exception {
        X86TypeInfoProgramBuilder builder = new X86TypeInfoProgramBuilder();
        validationTest(builder);
    }

    @Test
    public void defaultLocationTest() throws Exception {
        X86TypeInfoProgramBuilder builder = new X86TypeInfoProgramBuilder();
        locationTest(builder);
    }

    @Test
    public void ppc64LocationTest() throws Exception {
        Ppc64TypeInfoProgramBuilder builder = new Ppc64TypeInfoProgramBuilder();
        locationTest(builder);
    }

    @Test
    public void ppc64ValidationTest() throws Exception {
        Ppc64TypeInfoProgramBuilder builder = new Ppc64TypeInfoProgramBuilder();
        validationTest(builder);
    }
}
