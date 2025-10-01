import { useForm } from "react-hook-form";
import type z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";

import type { components } from "@/generated/types";
import { employeeSchema } from "@/schema/employee";
import {
  useCreateEmployeeMutation,
  useUpdateEmployeeMutation,
} from "@/lib/tanstack-query/mutations";

import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

function EmployeeForm({
  initialValues,
  onCancel,
  onSuccess,
}: {
  onSuccess?: (id?: string) => void;
  onCancel?: () => void;
  initialValues?: components["schemas"]["EmployeeResponseDto"];
}) {
  const form = useForm<z.infer<typeof employeeSchema>>({
    resolver: zodResolver(employeeSchema),
    defaultValues: {
      email: initialValues?.email,
      name: initialValues?.name,
      position: initialValues?.position,
    },
  });
  const updateEmployeeMutation = useUpdateEmployeeMutation();
  const createEmployeeMutation = useCreateEmployeeMutation();

  const isPending =
    createEmployeeMutation.isPending || updateEmployeeMutation.isPending;
  const isEdit = !!initialValues?.id;

  const onSubmit = (values: z.infer<typeof employeeSchema>) => {
    if (isEdit) {
      updateEmployeeMutation.mutate(
        {
          body: {
            ...values,
          },
          params: {
            path: {
              id: initialValues?.id,
            },
          },
        },
        {
          onSuccess: () => onSuccess?.(),
        }
      );
    } else {
      createEmployeeMutation.mutate(
        { body: values },
        { onSuccess: (data) => onSuccess?.(data.id) }
      );
    }
  };

  return (
    <Form {...form}>
      <form className="space-y-4" onSubmit={form.handleSubmit(onSubmit)}>
        <FormField
          name="name"
          control={form.control}
          render={({ field }) => {
            return (
              <FormItem>
                <FormLabel>Name</FormLabel>
                <FormControl>
                  <Input placeholder="Employee name" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            );
          }}
        />
        <FormField
          name="email"
          control={form.control}
          render={({ field }) => {
            return (
              <FormItem>
                <FormLabel>Email</FormLabel>
                <FormControl>
                  <Input placeholder="Email" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            );
          }}
        />
        <FormField
          name="position"
          control={form.control}
          render={({ field }) => {
            return (
              <FormItem>
                <FormLabel>Position</FormLabel>
                <FormControl>
                  <Input placeholder="Employee position" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            );
          }}
        />
        <div className="flex justify-between gap-x-2 ">
          {onCancel && (
            <Button
              variant={"outline"}
              disabled={isPending}
              type="button"
              onClick={() => {
                onCancel();
              }}
            >
              Cancel
            </Button>
          )}
          <Button type="submit" disabled={isPending} className="ml-2">
            {isEdit ? "Update" : "Create"}
          </Button>
        </div>
      </form>
    </Form>
  );
}

export { EmployeeForm };
