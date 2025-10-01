import type { components } from "@/generated/types";

import { ResponsiveDialog } from "@/components/ui/responsive-dialog";
import { EmployeeForm } from "@/components/employee-form";

interface UpdateEmployeeDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  initialValues: components["schemas"]["EmployeeResponseDto"];
}

export const UpdateEmployeeDialog = ({
  open,
  onOpenChange,
  initialValues,
}: UpdateEmployeeDialogProps) => {
  return (
    <ResponsiveDialog
      title="Edit Employee"
      description="Edit the Employee details"
      open={open}
      onOpenChange={onOpenChange}
    >
      <EmployeeForm
        onSuccess={() => onOpenChange(false)}
        onCancel={() => onOpenChange(false)}
        initialValues={initialValues}
      />
    </ResponsiveDialog>
  );
};
