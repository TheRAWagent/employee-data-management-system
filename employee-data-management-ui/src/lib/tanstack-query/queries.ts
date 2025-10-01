import createFetchClient from "openapi-fetch";
import createClient from "openapi-react-query";

import { type paths } from "@/generated/types";

const client = createFetchClient<paths>({
  baseUrl: import.meta.env.VITE_API_URL!,
});

const $api = createClient(client);

export function useEmployees({
  page,
  search,
  size,
}: {
  page?: number;
  size?: number;
  search?: string;
}) {
  return $api.useQuery(
    "get",
    "/api/employees",
    {
      params: {
        query: {
          page: page ?? undefined,
          search: search ?? undefined,
          size: size ?? undefined,
        },
      },
      headers: {
        "Content-Type": "application/json",
      },
    },
    {
      staleTime: 1000 * 60 * 30,
    }
  );
}

export function useEmployeeById(employeeId: string) {
  return $api.useQuery(
    "get",
    "/api/employees",
    {
      params: {
        query: {
          employeeId,
        },
      },
    },
    {
      staleTime: 1000 * 60 * 30,
    }
  );
}
