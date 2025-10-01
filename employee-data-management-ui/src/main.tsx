import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

import App from "@/App.tsx";
import { Toaster } from "@/components/ui/sonner";

import "./index.css";
const queryClient = new QueryClient();

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <div className="max-w-screen-2xl mx-auto">
        <App />
        <Toaster />
      </div>
      <ReactQueryDevtools />
    </QueryClientProvider>
  </StrictMode>
);
