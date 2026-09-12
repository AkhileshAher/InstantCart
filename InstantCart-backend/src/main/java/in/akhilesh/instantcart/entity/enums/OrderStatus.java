package in.akhilesh.instantcart.entity.enums;

public enum OrderStatus {
    PLACED,
    CONFIRMED,
    OUT_FOR_DELIVERY,
    ASSIGNED,
    PACKED,
    DELIVERED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus newStatus) {
        return switch (this) {

            case PLACED ->
                    newStatus == OrderStatus.PACKED ||
                            newStatus == OrderStatus.CANCELLED;

            case PACKED ->
                    newStatus == OrderStatus.CONFIRMED ||
                            newStatus == OrderStatus.CANCELLED;

            case CONFIRMED ->
                    newStatus == OrderStatus.ASSIGNED ||
                            newStatus == OrderStatus.CANCELLED;

            case ASSIGNED ->
                    newStatus == OrderStatus.OUT_FOR_DELIVERY;

            case OUT_FOR_DELIVERY ->
                    newStatus == OrderStatus.DELIVERED ||
                            newStatus == OrderStatus.CANCELLED;

            case DELIVERED, CANCELLED -> false;
        };
    }
}
