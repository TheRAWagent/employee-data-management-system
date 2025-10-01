import { type paths } from "@/generated/types";
import { useQueryClient } from "@tanstack/react-query";
import createFetchClient from "openapi-fetch";
import createClient from "openapi-react-query";
import { toast } from "sonner";

const client = createFetchClient<paths>({
  baseUrl: import.meta.env.VITE_API_URL,
});
const $api = createClient(client);

export function useCreateEmployeeMutation() {
  const queryClient = useQueryClient();
  return $api.useMutation("post", "/api/employees", {
    onSuccess: () => toast.success("Employee created successfully"),
    onError: () => toast.error("Failed to create employee"),
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ["get", "/api/employees"] });
    },
  });
}

export function useUpdateEmployeeMutation() {
  const queryClient = useQueryClient();
  return $api.useMutation("patch", "/api/employees/{id}", {
    onSuccess: () => toast.success("Employee updated successfully"),
    onError: () => toast.error("Failed to update employee"),
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ["get", "/api/employees"]});
    },
  });
}

export function useDeleteEmployeeMutation() {
  const queryClient = useQueryClient();
  return $api.useMutation("delete", "/api/employees/{id}", {
    onSuccess: () => toast.success("Employee deleted successfully"),
    onError: () => toast.error("Failed to delete employee"),
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ["get", "/api/employees"] });
    },
  });
}
