import type { ColumnDef } from "@tanstack/react-table";

import type { components } from "@/generated/types";

import { Actions } from "@/components/row-actions";

export type Employee = components["schemas"]["EmployeeResponseDto"];

export const columns: ColumnDef<Employee>[] = [
  {
    accessorKey: "name",
    header: "Name",
  },
  {
    accessorKey: "position",
    header: "Position",
  },
  {
    accessorKey: "email",
    header: "Email",
  },
  {
    id: "actions",
    header: "",
    cell: Actions,
  },
];
