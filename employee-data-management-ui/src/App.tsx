import { Loader2, Plus } from "lucide-react";
import { useMemo, useState } from "react";

import { useEmployees } from "@/lib/tanstack-query/queries";

import { Button } from "@/components/ui/button";
import { DataTable } from "@/components/ui/data-table";
import { columns } from "@/components/columns";
import { DataPagination } from "@/components/data-pagination";
import { NewMeetingDialog } from "@/components/new-employee-dialog";
import { Separator } from "@/components/ui/separator";
import { Search } from "@/components/search";

function App() {
  const [employeeDialogOpen, setEmployeeDialogOpen] = useState(false);
  const [page, setPage] = useState(1);
  const [search, setSearch] = useState("");
  const [searchTerm, setSearchTerm] = useState<string | undefined>(undefined);
  const { data, isPending } = useEmployees({ page, search: searchTerm });

  const tableData = useMemo(() => data?.content ?? [], [data?.content]);
  return (
    <div className="p-5">
      <NewMeetingDialog
        onOpenChange={(open) => setEmployeeDialogOpen(open)}
        open={employeeDialogOpen}
      />
      <div className="w-full space-y-4">
        {/* Title Section */}
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
          <h1 className="text-2xl font-semibold tracking-tight text-stone-800">
            Employee Management
          </h1>
          <Button
            className="flex items-center gap-2"
            onClick={() => setEmployeeDialogOpen(true)}
          >
            <Plus className="h-4 w-4" />
            Add Employee
          </Button>
        </div>

        <Search
          value={search}
          onChange={setSearch}
          onReset={() => {
            setSearch("");
            setSearchTerm(undefined);
            setPage(1);
          }}
          onSearch={() => {
            setSearchTerm(search);
            setPage(1);
          }}
        />
      </div>
      <Separator className="my-5" />
      {isPending ? (
        <div className="flex items-center justify-center">
          <Loader2 />
        </div>
      ) : (
        <div>
          <DataTable
            columns={columns}
            data={tableData}
            getRowId={(row) => row.id}
          />
          <DataPagination
            onPageChange={(page) => setPage(page)}
            page={page}
            totalPages={data?.page?.totalPages ?? 0}
          />
        </div>
      )}
    </div>
  );
}

export default App;
