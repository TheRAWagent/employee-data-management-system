import { Card, CardContent } from "@/components/ui/card";
import { Search as SearchIcon } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

export function Search({
  onChange,
  onReset,
  onSearch,
  value,
}: {
  value: string;
  onChange: (search: string) => void;
  onSearch: () => void;
  onReset: () => void;
}) {
  {
    /* Filters/Search Section */
  }
  return (
    <Card className="rounded-2xl shadow-sm bg-stone-50 border-stone-200 py-3">
      <CardContent className="px-4">
        <div className="flex flex-col sm:flex-row sm:items-center gap-3">
          <div className="relative w-full sm:max-w-sm">
            <SearchIcon className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-stone-500" />
            <Input
              placeholder="Search employees..."
              className="pl-9 bg-white"
              value={value}
              onChange={(e) => onChange(e.target.value)}
            />
          </div>
          <div className="flex gap-2 ml-auto">
            <Button
              variant="outline"
              className="text-stone-700"
              onClick={onReset}
            >
              Reset
            </Button>
            <Button onClick={onSearch}>Search</Button>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
