import { lazy, Suspense } from "react";

import { ResponsiveDialog } from "@/components/ui/responsive-dialog";

const EmployeeForm = lazy(() =>
  import("@/components/employee-form").then((m) => ({
    default: m.EmployeeForm,
  }))
);

interface NewEmployeeDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export const NewEmployeeDialog = ({
  open,
  onOpenChange,
}: NewEmployeeDialogProps) => {
  return (
    <ResponsiveDialog
      title="New Employee"
      description="Add a new employee"
      open={open}
      onOpenChange={onOpenChange}
    >
      <Suspense fallback={null}>
        <EmployeeForm
          onSuccess={() => onOpenChange(false)}
          onCancel={() => onOpenChange(false)}
        />
      </Suspense>
    </ResponsiveDialog>
  );
};
