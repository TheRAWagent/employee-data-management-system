import { z } from "zod";

export const employeeSchema = z.object({
  email: z.email(),
  name: z.string().min(1, "Name is required"),
  position: z.string().min(1, "Position is required"),
});
