interface RazorpayOptions {
    key: string;
    amount: number;
    currency: string;
    name: string;
    description: string;
    order_id: string;
    handler: (response: any) => void;
    modal?: {
        ondismiss?: () => void;
    };
    theme?: {
        color?: string;
    };
}

interface RazorpayInstance {
    open: () => void;
}

interface Window {
    Razorpay: new (options: RazorpayOptions) => RazorpayInstance;
}