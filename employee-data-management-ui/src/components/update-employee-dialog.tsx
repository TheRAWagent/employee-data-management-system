import { lazy, Suspense } from "react";

import type { components } from "@/generated/types";

import { ResponsiveDialog } from "@/components/ui/responsive-dialog";

const EmployeeForm = lazy(() =>
  import("@/components/employee-form").then((m) => ({
    default: m.EmployeeForm,
  }))
);

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
      <Suspense fallback={null}>
        <EmployeeForm
          onSuccess={() => onOpenChange(false)}
          onCancel={() => onOpenChange(false)}
          initialValues={initialValues}
        />
      </Suspense>
    </ResponsiveDialog>
  );
};
