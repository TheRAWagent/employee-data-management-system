import { EllipsisVertical, PencilIcon, TrashIcon } from "lucide-react";
import { useState } from "react";
import type { Row } from "@tanstack/react-table";

import type { Employee } from "@/components/columns";
import { useDeleteEmployeeMutation } from "@/lib/tanstack-query/mutations";

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { UpdateEmployeeDialog } from "@/components/update-employee-dialog";
import { useConfirm } from "@/hooks/use-confirm";

export function Actions({ row }: { row: Row<Employee> }) {
  const [isUpdateEmployeeDialogOpen, setIsUpdateEmployeeDialogOpen] =
    useState(false);
  const deleteEmployeeMutation = useDeleteEmployeeMutation();
  const [RemoveConfirmation, confirmRemove] = useConfirm(
    "Are you Sure?",
    "This will remove the employee and all its data. This action cannot be undone."
  );

  const handleRemoveEmployee = async () => {
    const ok = await confirmRemove();
    if (!ok) return;
    deleteEmployeeMutation.mutate({
      params: {
        path: {
          id: row.original.id,
        },
      },
    });
  };

  return (
    <>
      <RemoveConfirmation />
      <UpdateEmployeeDialog
        initialValues={row.original}
        onOpenChange={(open) => setIsUpdateEmployeeDialogOpen(open)}
        open={isUpdateEmployeeDialogOpen}
      />
      <DropdownMenu>
        <DropdownMenuTrigger>
          <EllipsisVertical />
        </DropdownMenuTrigger>
        <DropdownMenuContent>
          <DropdownMenuItem
            className="flex items-center gap-x-2"
            onClick={() => setIsUpdateEmployeeDialogOpen(true)}
          >
            <PencilIcon className="text-green-500" /> Edit
          </DropdownMenuItem>
          <DropdownMenuItem
            className="flex items-center gap-x-2"
            onClick={handleRemoveEmployee}
          >
            <TrashIcon className="text-red-500" />
            Delete
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </>
  );
}
