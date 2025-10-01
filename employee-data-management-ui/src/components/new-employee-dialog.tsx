import { ResponsiveDialog } from "@/components/ui/responsive-dialog";
import { EmployeeForm } from "@/components/employee-form";

interface NewMeetingDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export const NewMeetingDialog = ({
  open,
  onOpenChange,
}: NewMeetingDialogProps) => {
  return (
    <ResponsiveDialog
      title="New Employee"
      description="Add a new employee"
      open={open}
      onOpenChange={onOpenChange}
    >
      <EmployeeForm
        onSuccess={() => onOpenChange(false)}
        onCancel={() => onOpenChange(false)}
      />
    </ResponsiveDialog>
  );
};
