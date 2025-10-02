import { Loader2, Plus } from "lucide-react";
import { lazy, Suspense, useEffect, useMemo, useState } from "react";

import { useEmployees } from "@/lib/tanstack-query/queries";
import type { Employee } from "@/components/columns";

import { Button } from "@/components/ui/button";
import { DataPagination } from "@/components/data-pagination";
import { Separator } from "@/components/ui/separator";
import { Search } from "@/components/search";

const NewEmployeeDialog = lazy(() =>
  import("@/components/new-employee-dialog").then((m) => ({
    default: m.NewEmployeeDialog,
  }))
);

const DataTableWithColumns = lazy(async () => {
  const [{ DataTable }, { columns }] = await Promise.all([
    import("@/components/ui/data-table"),
    import("@/components/columns"),
  ]);

  return {
    default: ({ data }: { data: Employee[]; isPending: boolean }) => (
      <DataTable
        columns={columns}
        data={data}
        getRowId={(row: Employee) => row.id}
      />
    ),
  };
});

function App() {
  const [employeeDialogOpen, setEmployeeDialogOpen] = useState(false);
  const [page, setPage] = useState(1);
  const [search, setSearch] = useState("");
  const [searchTerm, setSearchTerm] = useState<string | undefined>(undefined);
  const { data, isPending } = useEmployees({ page, search: searchTerm });

  const tableData = useMemo(() => data?.content ?? [], [data?.content]);

  const prefetchDialog = () => {
    import("@/components/new-employee-dialog");
  };

  const prefetchTable = () => {
    Promise.all([
      import("@/components/ui/data-table"),
      import("@/components/columns"),
    ]);
  };

  useEffect(() => {
    if (!isPending) {
      prefetchTable();
    }
  }, [isPending]);

  return (
    <div className="p-5">
      <Suspense fallback={null}>
        {employeeDialogOpen && (
          <NewEmployeeDialog
            onOpenChange={(open) => setEmployeeDialogOpen(open)}
            open={employeeDialogOpen}
          />
        )}
      </Suspense>
      <div className="w-full space-y-4">
        {/* Title Section */}
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
          <h1 className="text-2xl font-semibold tracking-tight text-stone-800">
            Employee Management
          </h1>
          <Button
            className="flex items-center gap-2"
            onClick={() => setEmployeeDialogOpen(true)}
            onMouseEnter={prefetchDialog}
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
      <div>
        <Suspense
          fallback={
            <div className="flex items-center justify-center">
              <Loader2 />
            </div>
          }
        >
          <DataTableWithColumns data={tableData} isPending={isPending} />
        </Suspense>
        <DataPagination
          onPageChange={(page) => setPage(page)}
          page={page}
          totalPages={data?.page?.totalPages ?? 0}
        />
        {isPending && (
          <div className="flex items-center justify-center">
            <Loader2 />
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
